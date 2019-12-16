import React, {Component} from 'react';
import {Badge} from "react-bootstrap";

class TagViewer extends Component {

  render() {
    return (
        <React.Fragment>
          {
            this.props.value && this.props.value.map((skill, index) => {
              return (
                  <Badge key={skill.id}
                         className={"badgeSpace"} pill
                         variant="secondary">
                    {skill.shortName}
                  </Badge>

              )

          })}
        </React.Fragment>)
  }
}

export default TagViewer;