package app.management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;
import fileio.User;
import utils.Constants;
import utils.Errors;

import java.util.ArrayList;

public final class Upgrades {
    private ArrayList<User> users;
    private Pages page;
    private Errors err = Errors.getErrorsInstance();

    public Upgrades(final ArrayList<User> users, final Pages page) {
        this.users = users;
        this.page = page;
    }
    /**
     * action for buying tokens
     */
    public void buyTokens(final Action action, final ArrayNode output) {
        int nrTokens = getNumTokens();
        int newBalance = Integer.parseInt(users.get(page.getCurrentUserIdx()).
                getCredentials().getBalance());
        int count = Integer.parseInt(action.getCount());
        if (newBalance < count) {
            err.pageErr(output);
            return;
        }
        newBalance -= count;
        nrTokens += count;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().
                setBalance(Integer.toString(newBalance));
    }

    /**
     * action for buying premium account
     */
    public void buyPremiumAccount(final ArrayNode output) {
        int nrTokens = getNumTokens();
        if (nrTokens < Constants.PREMIUM_PRICE) {
            err.pageErr(output);
            return;
        }
        nrTokens -= Constants.PREMIUM_PRICE;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().setAccountType("premium");
    }

    /**
     * @return number of token available for current user
     */
    public int getNumTokens() {
        return users.get(page.getCurrentUserIdx()).getTokensCount();
    }
}
