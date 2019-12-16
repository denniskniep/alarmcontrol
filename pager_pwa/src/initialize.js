import React, {Component} from 'react';
import 'firebaseui/dist/firebaseui.css';
import Grid from "@material-ui/core/Grid";
import CircularProgress from "@material-ui/core/CircularProgress";
import Modal from "@material-ui/core/Modal";

class Initialize extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Modal

            disablePortal
            disableEnforceFocus
            disableAutoFocus
            open
            aria-labelledby="server-modal-title"
            aria-describedby="server-modal-description"

        >
          <Grid
              container
              direction="row"
              justify="center"
              alignItems="center"
              style={{height: "100%"}}>

            <div>
              <CircularProgress style={{margin:"12px"}}/>
              <h3>Loading...</h3>
            </div>
          </Grid>
        </Modal>

    )
  }
}

export default Initialize