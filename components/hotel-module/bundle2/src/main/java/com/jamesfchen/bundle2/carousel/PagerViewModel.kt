package jamesfchen.widget

import jamesfchen.widget.carousel.TabsLayout

/**
 * Copyright ® $ 2019
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: jamesfchen@gmail.com
 * @since: Jun/16/2019  Sun
 */
data class PagerViewModel(
        val tab: TabsLayout.TabItem,
        val contents: List<Any>
)