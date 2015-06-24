package org.denigma.drugage.domain

import scala.util.{Failure, Success, Try}

import org.w3.banana.binder.{FromPG, ToPG, PGBinder, FromLiteral, ToLiteral, RecordBinder}
import org.w3.banana.{FailedConversion, PointedGraph, RDF, RDFOps, RDFStore, SparqlOps}
import squants.motion.MassFlow
import squants.time.Time

import org.denigma.drugage.domain.aux.DrugsNS

class BananaDrugRepository[Rdf <: RDF, Connection](connection: Connection)
  (implicit rdfOps: RDFOps[Rdf], rdfStore: RDFStore[Rdf, Try, Connection], sparqlOps: SparqlOps[Rdf], recordBinder: RecordBinder[Rdf])
  extends DrugRepository {

  import rdfOps._

  private[this] val drugsNS = DrugsNS()

  private[this] object Binders {

    import recordBinder._

    class SerializableToLiteral[T] extends ToLiteral[Rdf, T] {
      override def toLiteral(t: T): Rdf#Literal = Literal(t.toString, xsd.string)
    }
    class SerializableFromLiteral[T](builder: String => Try[T]) extends FromLiteral[Rdf, T] {
      override def fromLiteral(literal: Rdf#Literal): Try[T] = {
        val Literal(lexicalForm, datatype, _) = literal
          // some RDF stores may return null for datatype
        if (datatype == xsd.string || datatype == null) // scalastyle:ignore null
          builder(lexicalForm)
        else
          Failure(FailedConversion(s"$literal is not an xsd:string"))
      }
    }

    implicit val genderToLiteral = new SerializableToLiteral[Gender]
    implicit val literalToGender = new SerializableFromLiteral[Gender]({
      case "male" => Success(MALE)
      case "female" => Success(FEMALE)
      case s: Any => Failure(FailedConversion(s"Unknown value for gender: $s")) // scalastyle:ignore multiple.string.literals
    })
    val genderBinder = implicitly[PGBinder[Rdf, Gender]]

    val specie = property[String](drugsNS.specie)
    val strain = optional[String](drugsNS.strain)
    val gender = optional[Gender](drugsNS.gender)
    implicit val organismBinder = pgb[Organism](specie, strain, gender)(Organism.apply, Organism.unapply)

    implicit val massFlowToLiteral = new SerializableToLiteral[MassFlow]
    implicit val literalToMassFlow = new SerializableFromLiteral[MassFlow](MassFlow.apply)
    val massFlowBinder = implicitly[PGBinder[Rdf, MassFlow]]

    implicit val statisticToLiteral = new SerializableToLiteral[Statistic]
    implicit val literalToStatistic = new SerializableFromLiteral[Statistic]({
      case "mean" => Success(MEAN)
      case "median" => Success(MEDIAN)
      case "max" => Success(MAX)
      case s: Any => Failure(FailedConversion(s"Unknown value for statistic: $s"))
    })
    val statisticBinder = implicitly[PGBinder[Rdf, Statistic]]

    // TODO: it's better to store it as xsd:duration
    implicit val timeToLiteral = new SerializableToLiteral[Time]
    implicit val literalToTime = new SerializableFromLiteral[Time](Time.apply)
    val timeBinder = implicitly[PGBinder[Rdf, Time]]

    implicit object lifespanChangesToPG extends ToPG[Rdf, (Statistic, LifespanChange)] {
      override def toPG(t: (Statistic, LifespanChange)): PointedGraph[Rdf] = t match {
        case (s, ChangesBy(ratio)) => (
          bnode()
          -- drugsNS.statistic ->- s
          -- drugsNS.changes_lifespan_by ->- ratio
        )
        case (s, IncreasesAtLeastBy(delta)) => (
          bnode()
          -- drugsNS.statistic ->- s
          -- drugsNS.increases_lifespan_at_least_by ->- delta
        )
        case (s, IncreasesUpTo(age)) => (
          bnode()
          -- drugsNS.statistic ->- s
          -- drugsNS.increases_lifespan_up_to ->- age
        )
      }
    }

    // TODO: is it even possible to implement it by pattern matching?
    implicit object lifespanChangesFromPG extends FromPG[Rdf, (Statistic, LifespanChange)] {
      override def fromPG(pointed: PointedGraph[Rdf]): Try[(Statistic, LifespanChange)] = {
        val statistic: Statistic = (pointed / drugsNS.statistic).as[Statistic].get
        if ((pointed / drugsNS.changes_lifespan_by).nonEmpty) {
          val change = ChangesBy((pointed / drugsNS.changes_lifespan_by).as[Double].get)
          Success((statistic, change))
        } else if ((pointed / drugsNS.increases_lifespan_at_least_by).nonEmpty) {
          val change = IncreasesAtLeastBy((pointed / drugsNS.increases_lifespan_at_least_by).as[Time].get)
          Success((statistic, change))
        } else if ((pointed / drugsNS.increases_lifespan_up_to).nonEmpty) {
          val change = IncreasesUpTo((pointed / drugsNS.increases_lifespan_up_to).as[Time].get)
          Success((statistic, change))
        } else {
          Failure(FailedConversion(s"Can't reconstruct lifespan change from $pointed"))
        }
      }
    }

    val lifespanChangesBinder = implicitly[PGBinder[Rdf, (Statistic, LifespanChange)]]

    val name = property[String](drugsNS.compound_name)
    val synonyms = set[String](drugsNS.synonym)
    val organism = property[Organism](drugsNS.organism)
    val dosage = property[MassFlow](drugsNS.dosage)
    val lifespanChanges = set[(Statistic, LifespanChange)](drugsNS.lifespanChange)
    val pubmedId = property[String](drugsNS.pubmed_id)
    val notes = optional[String](drugsNS.notes)
    val nonSignificant = property[Boolean](drugsNS.nonSignificant)
    val id = property[DrugId](drugsNS.drugId)

    def deserialize(name: String, synonyms: Set[String], organism: Organism, dosage: MassFlow,
      lifespanChanges: Set[(Statistic, LifespanChange)], pubmedId: String, notes: Option[String],
      nonSignificant: Boolean, id: DrugId): Drug =

      Drug(name, synonyms, organism, dosage, lifespanChanges.toMap, pubmedId, notes, nonSignificant, id)

    def serialize(d: Drug): Option[(String, Set[String], Organism, MassFlow, Set[(Statistic, LifespanChange)],
      String, Option[String], Boolean, DrugId)] =

      Some(d.compoundName, d.synonyms, d.organism, d.dosage, d.lifespanChanges.toSet, d.pubmedId, d.notes, d.nonSignificant, d.id)

    implicit val drugBinder = pgbWithId[Drug](d => drugsNS.drugs / d.id)(name, synonyms, organism, dosage,
      lifespanChanges, pubmedId, notes, nonSignificant, id)(deserialize, serialize)
  }

  import Binders.drugBinder

  override def getById(id: DrugId): Option[Drug] = {
    val g = rdfStore.getGraph(connection, drugsNS.drugs / id).get
    if (g.size == 0) {
      None
    } else {
      val v = PointedGraph(drugsNS.drugs / id, g).as[Drug]
      Some(v.get)
    }
  }

  override def delete(id: DrugId): Unit = {
    rdfStore.removeGraph(connection, drugsNS.drugs / id)
  }

  override def upsert(drug: Drug): Unit = {
    delete(drug.id)  // this is for upsert semantics. Probably must be surrounded with transaction

    val pg = drug.toPointedGraph
    val result = rdfStore.appendToGraph(connection, drugsNS.drugs / drug.id, pg.graph)
    result.get  // just to throw an exception for Failure
  }
}
