package blackjackgame.model.card

import blackjackgame.model.card.Card
import blackjackgame.model.card.Denomination
import blackjackgame.model.card.Suit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CardTest {

    @Test
    internal fun `test init card`() {
        val card = Card(Suit.HEART, Denomination.TEN)

        assertThat(card).isEqualTo(Card(Suit.HEART, Denomination.TEN))
    }
}