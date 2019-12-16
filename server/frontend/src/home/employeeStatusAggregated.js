import React, {Component} from 'react';
import {Badge, Col, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import EmployeeStates from "./employeeStates";

class EmployeeStatusAggregated extends Component {

  getLengthForFiltered(employees, by) {

    if (!employees) {
      return 0;
    }

    return employees.filter(e => e.status && e.status.status == by).length;
  }

  render() {

    return (<Row>
          <Col>
            <h2>
              <Badge variant={"success"}>
                <div>
                  <FontAwesomeIcon className={"insideBadge"}
                                   icon={["far", "check-circle"]}/>
                  <Badge
                      variant="light">{
                    this.getLengthForFiltered(
                        this.props.employees,
                        EmployeeStates.getAvailable())
                  }</Badge>
                </div>
              </Badge>
            </h2>
          </Col>
          <Col>
            <h2>
              <Badge variant={"danger"}>
                <div>
                  <FontAwesomeIcon className={"insideBadge"}
                                   icon={["fas", "times"]}/>
                  <Badge
                      variant="light">{
                    this.getLengthForFiltered(
                        this.props.employees,
                        EmployeeStates.getNotAvailable())
                  }</Badge>
                </div>
              </Badge>
            </h2>
          </Col>
        </Row>
    );
  }
}

export default EmployeeStatusAggregated