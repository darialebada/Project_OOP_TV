package management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;
import fileio.User;
import pages.Page;
import utils.Constants;
import utils.Errors;

import java.util.ArrayList;

public final class Upgrades {
    private final ArrayList<User> users;
    private final Page page;
    private final Errors err = Errors.getErrorsInstance();

    public Upgrades(final ArrayList<User> users, final Page page) {
        this.users = users;
        this.page = page;
    }
    /**
     * action for buying tokens
     */
    public void buyTokens(final Action action, final ArrayNode output, final int currentUserIdx) {
        int nrTokens = getNumTokens(currentUserIdx);
        int newBalance = Integer.parseInt(users.get(currentUserIdx).
                getCredentials().getBalance());
        int count = Integer.parseInt(action.getCount());
        if (newBalance < count) {
            err.pageErr(output);
            return;
        }
        newBalance -= count;
        nrTokens += count;
        users.get(currentUserIdx).setTokensCount(nrTokens);
        users.get(currentUserIdx).getCredentials().
                setBalance(Integer.toString(newBalance));
    }

    /**
     * action for buying premium account
     */
    public void buyPremiumAccount(final ArrayNode output, final int currentUserIdx) {
        int nrTokens = getNumTokens(currentUserIdx);
        if (nrTokens < Constants.PREMIUM_PRICE) {
            err.pageErr(output);
            return;
        }
        nrTokens -= Constants.PREMIUM_PRICE;
        users.get(currentUserIdx).setTokensCount(nrTokens);
        users.get(currentUserIdx).getCredentials().setAccountType("premium");
    }

    /**
     * @return number of token available for current user
     */
    public int getNumTokens(final int currentUserIdx) {
        return users.get(currentUserIdx).getTokensCount();
    }
}
