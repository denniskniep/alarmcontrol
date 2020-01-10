import React, {Component} from 'react';
import {CurrentConfigContext} from "./currentConfigContext";
import {loadConfig, saveConfig} from "./config";

const CONFIG_KEY = 'config';

class CurrentConfigContainer extends Component {

  constructor(props) {
    super(props);

    this.state = {
      config: loadConfig()
    }
  }

  setConfig(config) {
    if(config && config.apiKey && config.projectId && config.messagingSenderId && config.appId){
      let newConfig = {
        apiKey: config.apiKey,
        projectId: config.projectId,
        messagingSenderId: config.messagingSenderId,
        appId: config.appId
      };
      saveConfig(newConfig);
      this.setState({ config: newConfig})
    }else {
      console.log("config is not valid!")
    }
  }

  render() {
    return (
        <CurrentConfigContext.Provider value={{
          config: this.state.config,
          setConfig: (config) => this.setConfig(config)
        }}>
          {this.props.children}
        </CurrentConfigContext.Provider>
    )
  }
}

export default CurrentConfigContainer