import {createBrowserHistory} from "history";
import * as React from "react";
import * as ReactDOM from "react-dom";
import {ApolloProvider} from "react-apollo";
import {ApolloProvider as ApolloHooksProvider} from "@apollo/react-hooks";
import {Route, Router, Switch} from "react-router";
import {
  ConsoleLogger,
  LoggerContext,
  Environment,
  NopLogger,
} from "@tetherless-world/twxplore-base";
import {Provider} from "react-redux";
import store from "./store";
import {SelectionHome} from "./components/SelectionHome/SelectionHome";
import {Hrefs} from "./Hrefs";
import {apolloClient} from "./api/apolloClient";
import {NoRoute} from "./components/error/NoRoute";
import {MapLayout} from "./components/map/MapLayout";

// Logger
const logger = Environment.development ? new ConsoleLogger() : new NopLogger();

// Stores
const browserHistory = createBrowserHistory();

ReactDOM.render(
  <ApolloProvider client={apolloClient}>
    <ApolloHooksProvider client={apolloClient}>
      <LoggerContext.Provider value={logger}>
        <Provider store={store}>
          <Router history={browserHistory}>
            <Switch>
              <Route exact path={Hrefs.home} component={MapLayout} />
              <Route exact path={Hrefs.map} component={MapLayout} />
              <Route exact path={Hrefs.selection} component={SelectionHome} />
              <Route component={NoRoute} />
            </Switch>
          </Router>
        </Provider>
      </LoggerContext.Provider>
    </ApolloHooksProvider>
  </ApolloProvider>,
  document.getElementById("root")
);
