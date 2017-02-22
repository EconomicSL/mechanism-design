import java.util.UUID

import org.economicsl.mechanisms.{Alternative, Preference, SocialChoiceFunction}

import collection.{GenMap, GenSet}


case class Ballot(signature: UUID, alternative: Alternative) extends Preference[Alternative] {

  val ordering: Ordering[Alternative] = Ordering.by(a => if (a == alternative) 1 else 0)

}


type VoteCounts = GenMap[Alternative, Int]


class PluralityVotingRule(initial: VoteCounts) extends SocialChoiceFunction[Alternative, Ballot] {

  def apply(ballots: GenSet[Ballot]): Alternative = {
    val results = ballots.aggregate(initial)((counts, ballot) => update(counts, ballot), (m1, m2) => combine(m1, m2))
    val (winner, _) = results maxBy { case (_, votes) => votes }  // todo explicitly handle ties!
    winner
  }

  private[this] def update(current: VoteCounts, ballot: Ballot): VoteCounts = {
    val votes = current.getOrElse(ballot.alternative, 0)
    current.updated(ballot.alternative, votes + 1)
  }

  private[this] def combine(some: VoteCounts, other: VoteCounts): VoteCounts = {
    some.foldLeft(other){ case (counts, (candidate, votes)) =>
      val additionalVotes = other.getOrElse(candidate, 0)
      counts.updated(candidate, votes + additionalVotes)
    }
  }

}


// Quickly check that mechanism is working properly
val winner = new Alternative {}
val ballots = Set(Ballot(UUID.randomUUID(), new Alternative {}), Ballot(UUID.randomUUID(), winner), Ballot(UUID.randomUUID(), winner))
val votingRule = new PluralityVotingRule(Map.empty[Alternative, Int])
assert(winner == votingRule(ballots))





