import React, {Component} from 'react';
import {Badge, Col, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

class EmployeeFeedbackAggregated extends Component {

  aggregateSkills(skills, employeeFeedback) {
    let aggregatedSkills = {};

    skills.filter(s => s.displayAtOverview)
    .forEach(s => aggregatedSkills[s.id] = {
      skill: s,
      count: 0
    });

    employeeFeedback.forEach((ef) => {
      ef.employee.skills.forEach((s) => {
        if (aggregatedSkills[s.id]) {
          aggregatedSkills[s.id].count++;
        }
      });
    });
    return aggregatedSkills;
  }

  render() {
    let aggregatedSkills = this.aggregateSkills(this.props.skills, this.props.employeeFeedback);
    return (<Row>
          <Col>
            <h1>
              <Badge variant={this.props.badgeVariant}>
                <div>
                  <FontAwesomeIcon className={"insideBadge"}
                                   icon={this.props.icon}/>
                  <Badge
                      variant="light">{this.props.employeeFeedback.length}</Badge>
                </div>
              </Badge>
            </h1>
          </Col>
          {Object.keys(aggregatedSkills).map((skillId, index) => {

            let aggSkill = aggregatedSkills[skillId];

            return (
                <Col key={skillId} className={"d-flex align-items-center"}>
                  <h4>
                    <Badge variant={this.props.badgeVariant}>
                      <div>
                        <span className={"insideBadge"}>{aggSkill.skill.shortName}</span>
                        <Badge variant="light">{aggSkill.count}</Badge>
                      </div>
                    </Badge>
                  </h4>
                </Col>

            )
          })}
        </Row>
    );
  }
}

export default EmployeeFeedbackAggregated