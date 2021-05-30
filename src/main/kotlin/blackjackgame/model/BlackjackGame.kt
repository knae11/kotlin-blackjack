package blackjackgame.model

import blackjackgame.model.card.Card
import blackjackgame.model.card.Deck
import blackjackgame.model.player.Dealer
import blackjackgame.model.player.Player
import blackjackgame.model.player.Players
import blackjackgame.model.result.Result
import blackjackgame.model.result.getResult

class BlackjackGame(players: List<Player>, dealer: Player, private val deck: Deck) {
    private val participants: Players

    init {
        val playerGroup = mutableListOf(dealer)
        players.forEach { playerGroup.add(it) }
        participants = Players(playerGroup)
    }

    fun start() {
        participants.drawInitCards(deck)
    }

    fun getInitStatus(): List<Pair<String, List<Card>>> {
        return participants.map { Pair(it.name, it.getInitCards()) }
    }

    fun isExistHitPlayer(): Boolean {
        return participants.asSequence()
            .filter { it.isPlayer() }
            .any { it.canDraw() && it.isPlaying }
    }

    fun playTurn(answer: Boolean): Pair<String, List<Card>> {
        val player = findTurnPlayer()
        if (!answer) {
            player.endTurn()
            return Pair(player.name, player.cards.getCards())
        }
        player.drawCard(deck.handOutCard())
        return Pair(player.name, player.cards.getCards())
    }

    fun findTurnPlayer(): Player {
        return participants.asSequence()
            .filter { it.isPlayer() }
            .first { it.isHit() && it.isPlaying }
    }

    fun extractResult(): List<Triple<String, List<Card>, Int>> {
        return participants.map { Triple(it.name, it.cards.getCards(), it.cards.calculateFinalScore()) }
    }

    fun playDealerTurn(): Boolean {
        val dealer = participants.first { !it.isPlayer() } as Dealer
        if (dealer.isAvailableToDraw()) {
            dealer.drawCard(deck.handOutCard())
            return true
        }
        return false
    }

    fun extractWinLoseResult(): List<Pair<String, Result>> {
        val dealer: Dealer = participants.first { !it.isPlayer() } as Dealer
        val players = participants.filter { it.isPlayer() }

        val playerResult = players.map { Pair(it.name, getResult(dealer, it)) }
        val dealerResult = Pair(dealer.name, playerResult.reverse())

        val finalResult = mutableListOf(dealerResult)
        playerResult.forEach { finalResult.add(it) }

        return finalResult
    }

    private fun List<Pair<String, Result>>.reverse(): Result {
        val map: List<Result> = this.map { it.second }
        return map.reduce { a, b -> a + b }.reverse()
    }


}