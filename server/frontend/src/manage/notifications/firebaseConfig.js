import React, {Component} from 'react';
import QRCode from 'qrcode.react';
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";
import {Container, Row} from "react-bootstrap";

const QUERY_FIREBASE_CONFIG = gql`
query firebaseClientConfiguration{
  firebaseClientConfiguration{
    apiKey
    projectId
    messagingSenderId
    appId
    pagerUrl
  }
}
`;

class FirebaseConfig extends Component {

  render() {
    return (
        <React.Fragment>
          <QueryHandler query={QUERY_FIREBASE_CONFIG}
                        fetchPolicy="no-cache">
            {({data, refetch}) => {
              if (!data.firebaseClientConfiguration) {
                return (<React.Fragment>
                </React.Fragment>);
              }

              let config = {
                apiKey : data.firebaseClientConfiguration.apiKey,
                projectId : data.firebaseClientConfiguration.projectId,
                messagingSenderId : data.firebaseClientConfiguration.messagingSenderId,
                appId : data.firebaseClientConfiguration.appId,
              };

              let urlWithConfig = data.firebaseClientConfiguration.pagerUrl + "?config="  + encodeURIComponent(JSON.stringify(config))
              return (
                  <Container >

                    <Row className={"row-header"}>
                      <p>Scan the Barcode or follow the Link to <a target={"_blank"} href={urlWithConfig}>Pager</a></p>
                      <QRCode size={512} value={urlWithConfig} />
                    </Row>
                    <Row className={"row-header"}>
                    </Row>
                  </Container>
                  )
            }}</QueryHandler>
        </React.Fragment>
    )
  }
}
export default FirebaseConfig;