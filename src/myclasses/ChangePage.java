package myclasses;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;

public class ChangePage {
    public ChangePage() {
    }
    public Pages changePage(final Action action, final ArrayNode output, final int currentUserIdx) {
        Pages page = new Pages();
        switch (action.getPage()) {
           // case "login" -> page.
        }
        return page;
    }
}
