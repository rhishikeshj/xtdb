package xtdb.api.query.context

import clojure.lang.Keyword
import xtdb.api.query.domain.PullSpec
import xtdb.api.query.domain.PullSpec.Item
import xtdb.api.query.domain.PullSpec.Item.*
import xtdb.api.query.domain.PullSpec.Item.Field.Attributes.Companion.empty
import xtdb.api.underware.BuilderContext
import xtdb.api.underware.ComplexBuilderContext

class PullSpecContext private constructor(): ComplexBuilderContext<Item, PullSpec>(::PullSpec) {
    companion object : BuilderContext.Companion<PullSpec, PullSpecContext>(::PullSpecContext)

    operator fun Keyword.unaryPlus() = add(Field(this, empty))

    infix fun Keyword.with(block: PullFieldAttributesContext.() -> Unit) =
        add(Field(this, PullFieldAttributesContext.build(block)))

    fun join(keyword: Keyword, block: PullSpecContext.() -> Unit) =
        add(Join(keyword, build(block)))

    fun joinAll(keyword: Keyword) = add(Join(keyword, PullSpec(listOf(ALL))))
}