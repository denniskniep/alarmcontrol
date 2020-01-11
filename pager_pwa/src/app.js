import React, {Component} from 'react';
import Login from "./login";
import Subscription from "./subscription";
import Messages from "./messages";
import Menu from "./menu";
import CurrentUserContainer from "./auth/currentUserContainer";
import {CurrentUserContext} from "./auth/currentUserContext";
import CurrentConfigContainer from "./config/currentConfigContainer";
import {CurrentConfigContext} from "./config/currentConfigContext";

import Home from "./home";
import {HashRouter as Router, Route, Switch} from "react-router-dom";
import firebaseApp from "./firebaseApp";
import Initialize from "./initialize";
import InitConfig from "./initConfig";
import MessagesContainer from "./notifications/messagesContainer";

class App extends Component {

  constructor(props) {
    super(props);
  }

  getMenuItems(configContext, userContext) {
    if (!configContext.config) {
      return [
        {
          name: "Setup",
          href: "/setup"
        }];
    }

    if (!userContext.allSupportedAndGranted) {
      return [
        {
          name: "Home",
          href: "/home"
        }];
    }

    if (userContext.user) {
      return [
        {
          name: "Home",
          href: "/home"
        },
        {
          name: "Subscription",
          href: "/subscription"
        },
        {
          name: "Messages",
          href: "/messages"
        },
        {
          name: "Setup",
          href: "/setup"
        },
        {
          name: "Logout",
          onClick: () => firebaseApp.auth().signOut()
        }
      ];
    } else {
      return [{
        name: "Login",
        href: "/login"
      }];
    }
  }

  routes(configContext, userContext) {
    if (userContext.pendingUserAuth) {
      return (<Initialize/>)
    }

    if (!userContext.allSupportedAndGranted) {
      return (
          <Switch>
            <Route component={Home}/>
          </Switch>)
    }

    if (userContext.user) {
      return (
          <Switch>
            <Route exact path="/subscription" component={Subscription}/>
            <Route exact path="/messages" component={Messages}/>
            <Route exact path="/home" component={Home}/>
            <Route exact path="/setup" component={InitConfig}/>
            <Route component={Home}/>
          </Switch>
      )
    } else {
      return (
          <Switch>
            <Route component={Login}/>
          </Switch>)
    }
  }

  render() {
    return (
        <CurrentConfigContainer>
          <CurrentConfigContext.Consumer>
            {configContext => {
              return (
                  <React.Fragment>
                    {
                      !configContext.config &&
                      <Router>
                        <Menu items={[
                          {
                            name: "Setup",
                            href: "/setup"
                          }]} subscribed={false}>
                        <Switch>
                          <Route component={InitConfig}/>
                        </Switch>
                        </Menu>
                      </Router>
                    }

                    {
                      configContext.config &&
                      <CurrentUserContainer>
                        <CurrentUserContext.Consumer>
                          {userContext => {
                            return (
                                <MessagesContainer>
                                  <Router>
                                    <Menu
                                        items={this.getMenuItems(configContext,
                                            userContext)}
                                        subscribed={userContext.subscribed}>
                                      {
                                        this.routes(configContext, userContext)
                                      }
                                    </Menu>
                                  </Router>
                                </MessagesContainer>);
                          }}
                        </CurrentUserContext.Consumer>
                      </CurrentUserContainer>
                    }</React.Fragment>
              );
            }}
          </CurrentConfigContext.Consumer>
        </CurrentConfigContainer>
    )
  }
}

export default App