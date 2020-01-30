import React, {Component} from 'react';
import CurrentConfigContainer from "./config/currentConfigContainer";
import {CurrentConfigContext} from "./config/currentConfigContext";
import Grid from "@material-ui/core/Grid";
import QrReader from 'react-qr-reader'
import Button from "@material-ui/core/Button";
import {saveConfig} from "./config/config";

class InitConfig extends Component {

  constructor(props) {
    super(props);
  }

  handleScan(data, configContext) {
    if (data) {
      console.log("QR-Code scanned: ", data)
      let parsedConfig = JSON.parse(data);
      saveConfig(parsedConfig);
      window.location.reload();
    }
  }

  handleError(err) {
    console.error(err)
  }

  reload(){
    window.location.reload();
  }

  render() {
    return (
        <React.Fragment>
            <CurrentConfigContext.Consumer>
              {configContext => {
                return (
                    <React.Fragment>
                      <h1>Setup</h1>
                      <p> Scan Barcode with configuration:
                      </p>
                      <Grid container spacing={2}>
                        <Grid item xs={12} md={2}>
                          <QrReader
                              delay={300}
                              onError={(err) => this.handleError(err)}
                              onScan={(data) => this.handleScan(data, configContext)}
                          />
                          {configContext.config &&
                          <React.Fragment>
                            <p><b>Current Configuration:</b></p>
                            <p>ProjectId: {configContext.config.projectId}</p>
                            <p>ApiKey: {configContext.config.apiKey}</p>
                            <p>MessagingId: {configContext.config.messagingSenderId}</p>
                            <p>AppId: {configContext.config.appId}</p>

                            <Button variant="contained" color="primary"
                                    onClick={() => this.reload()}>
                              Reload App
                            </Button>
                          </React.Fragment>
                          }
                        </Grid>
                      </Grid>
                    </React.Fragment>);
              }}
            </CurrentConfigContext.Consumer>
        </React.Fragment>
    )
  }
}

export default InitConfig