package com.mabe.productions.prisiukantraste;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

/**
 * Created by Benas on 18/06/2017.
 */

public final class HeaderItem extends NewsItem implements StickyHeader {

    public HeaderItem(String title, int type) {
        super(title, type);
    }
}