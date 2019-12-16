import React, {Component} from 'react';
import Login from "./login";
import Subscription from "./subscription";
import Messages from "./messages";
import Menu from "./menu";
import CurrentUserContainer from "./auth/currentUserContainer";
import {CurrentUserContext} from "./auth/currentUserContext";

import Home from "./home";
import {HashRouter as Router, Route, Switch} from "react-router-dom";
import firebaseApp from "./firebaseApp";
import Initialize from "./initialize";

class App extends Component {

  constructor(props) {
    super(props);
  }

  getMenuItems(userContext) {
    if(!userContext.allSupportedAndGranted){
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

  routes(userContext) {
    if(userContext.pendingUserAuth){
      return( <Initialize/>)
    }

    if(!userContext.allSupportedAndGranted){
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
        <CurrentUserContainer>
          <CurrentUserContext.Consumer>
            {userContext => {
              return (
                  <Router>
                    <Menu items={this.getMenuItems(userContext)}
                          subscribed={userContext.subscribed}>
                      {
                        this.routes(userContext)
                      }
                    </Menu>
                  </Router>);
            }}
          </CurrentUserContext.Consumer>
        </CurrentUserContainer>)
  }
}

export default App