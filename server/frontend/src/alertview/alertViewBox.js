import React, {Component} from 'react';

class AlertViewBox extends Component {
  render() {
    return (<div className="alertViewOuterBox h-100">
      <div className={["alertViewBox", "h-100"].join(' ')}>
        {this.props.children}
      </div>
    </div>);
  }
}
export default AlertViewBox;