import React, {Component} from 'react';
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";
import EditableTable from "../../components/editableTable";
import TagViewer from "../../components/tagViewer";
import TagEditor from "../../components/draggableTagEditor";

const hessischeAaoKeywordlist = [
  {id: '6bcceb2f-d665-488e-99a6-54c87e3a06a2', keyword: 'F1 '},
  {id: 'de47acdb-5baa-4009-aaeb-da470b608ed6', keyword: 'F2'},
  {id: '625f8b3e-9f3b-45de-883d-4028e51cbc7c', keyword: 'F2Y'},
  {id: '11367cbb-a25c-4361-9257-029eb429dde0', keyword: 'F3'},
  {id: '93b1feea-1372-47b9-90af-4a35d63d4208', keyword: 'F3Y'},
  {id: '88301b7a-0918-4356-b8b3-5ea531d22ce1', keyword: 'F4'},
  {id: 'caf886f4-927c-4375-bbf7-9a0826814b41', keyword: 'FBMA'},
  {id: 'dd902298-6e92-44cd-b2f3-576d73e056bc', keyword: 'FBUSY'},
  {id: '582711bd-781c-4a1d-a020-502eecb736c9', keyword: 'FFLUG1Y'},
  {id: 'a3698d33-b41b-4bb7-9956-4de5d4eb5cb1', keyword: 'FFLUG2Y'},
  {id: 'a356bf39-8368-4f71-8e56-260b9150f3a3', keyword: 'FGAS1'},
  {id: '65832a01-1a2b-44ca-a016-35942b0a2dcb', keyword: 'FGAS2'},
  {id: '1ce6f1a3-690e-44ab-b8de-67fcc0169941', keyword: 'FLKW/FZug'},
  {id: '394bce18-31f9-4ea1-b937-844071ca1e57', keyword: 'FZugY'},
  {id: '35a64667-cd82-4904-b493-e739987c1a1f', keyword: 'FRWM'},
  {id: '4e7dab75-1747-4821-bad5-3b030d47d29a', keyword: 'FSCHIFF1'},
  {id: '4fe63f84-1ac2-432d-a4f0-043af0d37dd1', keyword: 'FSCHIFF2'},
  {id: 'd81316dd-5f6d-4415-a073-0131b7ee018a', keyword: 'FSCHIFF2Y'},
  {id: '6bdfbcc1-5fca-44c2-97a4-e9fa2e34ce7d', keyword: 'FSCHIFF2Gefahr'},
  {id: '33bc9ab0-9038-4e7d-8e05-2e415baaeead', keyword: 'FWALD1'},
  {id: '0204f123-e3ca-42a7-afb4-8d9a452254ee', keyword: 'FWALD2'},
  {id: '789f3d21-ad07-4d20-a182-dcc630495f9f', keyword: 'H1'},
  {id: '750da426-56f4-45ed-bad0-3e58ad372d35', keyword: 'H1Y'},
  {id: '633a9c75-1ff7-4aa4-b3e3-b0f03a730d7a', keyword: 'H2'},
  {id: 'cd59442c-f0c2-4e53-9329-fee029e94fbd', keyword: 'HABSTY'},
  {id: '522cf6d5-7ee6-4f24-b84e-b9b6fc667b17', keyword: 'HELEK'},
  {id: 'e09924be-29f3-4ef1-99f0-8980177f6787', keyword: 'HEINSTY'},
  {id: 'c07952ef-c1fd-4337-9942-0b5fcdc35572', keyword: 'HFLUSS'},
  {id: '4ee4ded8-e0e4-4b20-8364-3b2f6b3882e1', keyword: 'HFLUSSY'},
  {id: '7d7766cc-b37f-411a-845e-62ff6d31662e', keyword: 'HWASSY'},
  {id: '8a5cc882-691f-4e8d-9e13-31f10e506b02', keyword: 'HGAS1'},
  {id: '54ca19df-1c3b-41de-af4d-07b6fa902aee', keyword: 'HGAS2'},
  {id: '9bb0c576-5626-4176-9318-c60f73b04dc1', keyword: 'HGEFAHR1'},
  {id: 'c0afcb1e-9064-42a6-89ec-b9f891eaeded', keyword: 'HGEFAHR2'},
  {id: '13eed6a2-81f9-4cc1-8636-6a435f0c451d', keyword: 'HKLEMM1Y'},
  {id: 'df464a70-9de6-4307-bbda-a49c92ccee62', keyword: 'HKLEMM2Y'},
  {id: '57c1c13b-d404-4124-b80f-b6c32c6eb033', keyword: 'HÖLFLUSS'},
  {id: 'f94fab84-3ff9-4ce2-b368-4f156676212f', keyword: 'HÖLWASS'},
  {id: '521b6589-81b0-4b6e-8e9e-afe4c86c2041', keyword: 'HRADIOAKTIV'},
  {id: 'fadfd0ef-e154-4473-afa0-6d942ea3cd86', keyword: 'HSCHIFF'},
  {id: '7bec730f-5a6f-4126-8652-ae1c822d1c3c', keyword: 'HSCHIFFY'},
  {id: '7c79e383-0999-4759-be64-82fedc5f4ebf', keyword: 'HZUG1Y'},
  {id: 'c1a1526f-cb7c-4411-bff7-6ec751651979', keyword: 'HZUG2Y'}];

const ADD_AAO_CATALOG = gql`
mutation addCatalog($organisationId: ID!, $keywords : [CatalogKeywordInput]!){
  addCatalog(organisationId: $organisationId, keywords: $keywords)
}
`;

const DELETE_AAO = gql`
mutation deleteAao($organisationId: ID!, $uniqueAaoId: String!){
  deleteAao(
    organisationId: $organisationId,  
    uniqueAaoId : $uniqueAaoId)
}
`;

const ADD_AAO = gql`
mutation addAao($organisationId: ID!, $keywords : [String]!, $locations: [String]!, $vehicles: [String]!, $timeRangeNames: [String]!){
  addAao(organisationId: $organisationId, keywords: $keywords, locations: $locations, vehicles: $vehicles, timeRangeNames: $timeRangeNames ) {   
    uniqueId
  }
}
`;

const EDIT_AAO = gql`
mutation editAao($organisationId: ID!,$uniqueAaoId: String!, $keywords : [String]!, $locations: [String]!, $vehicles: [String]!, $timeRangeNames: [String]!){
  editAao(organisationId: $organisationId, uniqueAaoId: $uniqueAaoId, keywords: $keywords, locations: $locations, vehicles: $vehicles, timeRangeNames: $timeRangeNames ) {   
    uniqueId
  }
}
`;

class AaoEditMutation extends Component {

  constructor(props) {
    super(props);

    this.state = {};
  }

  render() {
    return (<MutationHandler mutation={DELETE_AAO}
                             onCompleted={() => this.props.onAaoRulesChanged
                                 && this.props.onAaoRulesChanged()}>
          {deleteAaoConfig => {
            return (
                <MutationHandler mutation={ADD_AAO}
                                 onCompleted={() => this.props.onAaoRulesChanged
                                     && this.props.onAaoRulesChanged()}>
                  {addAaoConfig => {
                    return (<MutationHandler mutation={EDIT_AAO}
                                             onCompleted={() => this.props.onAaoRulesChanged
                                                 && this.props.onAaoRulesChanged()}>
                          {editAaoConfig => {
                            return (<MutationHandler mutation={ADD_AAO_CATALOG}
                                                     onCompleted={() => this.props.onAaoRulesChanged
                                                         && this.props.onAaoRulesChanged()}>
                                  {addCatalog => {
                                    let vehicleSuggestions = this.props.vehicles.map(
                                        v => ({
                                          id: v.uniqueId,
                                          text: v.name,
                                          shortName: v.text
                                        }));
                                    let locationSuggestions = this.props.locations.map(
                                        v => ({
                                          id: v.uniqueId,
                                          text: v.name,
                                          shortName: v.text
                                        }));

                                    let aaoRules = this.props.aaoRules
                                        ? this.props.aaoRules : [];

                                    let keywords = hessischeAaoKeywordlist.map(
                                        entry => ({
                                          id: entry.id,
                                          text: entry.keyword,
                                          shortName: entry.keyword
                                        }));
                                    aaoRules = aaoRules
                                    .map(rule => ({
                                      uniqueId: rule.uniqueId,
                                      locations: this.props.locations.filter(
                                          l => rule.locations.includes(
                                              l.uniqueId)).map(l3 => ({
                                        id: l3.uniqueId,
                                        text: l3.name,
                                        shortName: l3.name
                                      })),
                                      keywords: keywords.filter(
                                          l => rule.keywords.includes(
                                              l.id)).map(l3 => ({
                                        id: l3.id,
                                        text: l3.text,
                                        shortName: l3.shortName
                                      })),
                                      vehicles: this.props.vehicles.filter(
                                          l => rule.vehicles.includes(
                                              l.uniqueId)).map(l3 => ({
                                        id: l3.uniqueId,
                                        text: l3.name,
                                        shortName: l3.name,
                                        position: rule.vehicles.indexOf(
                                            l3.uniqueId)
                                      })).sort(
                                          (a, b) => (a.position < b.position)
                                              ? -1 : 1),
                                      timeRangeNames: []
                                    }));
                                    return (
                                        <EditableTable data={aaoRules}
                                                       canView={false}
                                                       columns={[
                                                         {
                                                           key: "keywords",
                                                           name: "Alarmstichworte",
                                                           viewer: TagViewer,
                                                           editor: TagEditor,
                                                           editorProps: {
                                                             suggestions: keywords
                                                           },
                                                           defaultValue: []
                                                         },
                                                         {
                                                           key: "locations",
                                                           name: "Orte",
                                                           viewer: TagViewer,
                                                           editor: TagEditor,
                                                           editorProps: {
                                                             suggestions: locationSuggestions
                                                           },
                                                           defaultValue: []
                                                         },
                                                         {
                                                           key: "vehicles",
                                                           name: "Fahrzeuge",
                                                           viewer: TagViewer,
                                                           editor: TagEditor,
                                                           editorProps: {
                                                             suggestions: vehicleSuggestions
                                                           },
                                                           defaultValue: []
                                                         }
                                                       ]}

                                                       onNewRow={newRow => {
                                                         console.log('newRow',
                                                             newRow);
                                                         addCatalog({
                                                           variables: {
                                                             organisationId: this.props.organisationId,
                                                             keywords: hessischeAaoKeywordlist.map(
                                                                 entry => ({
                                                                   uniqueId: entry.id,
                                                                   keyword: entry.keyword
                                                                 }))
                                                           }
                                                         });
                                                         console.log(newRow);
                                                         addAaoConfig({
                                                           variables: {
                                                             organisationId: this.props.organisationId,
                                                             keywords: newRow.keywords.map(
                                                                 keyword => keyword.id),
                                                             locations: newRow.locations.map(
                                                                 location => location.id),
                                                             vehicles: newRow.vehicles.map(
                                                                 vehicle => vehicle.id),
                                                             timeRangeNames: []
                                                           }
                                                         })

                                                       }}

                                                       onRowEdited={(oldRow,
                                                           newRow) => {
                                                         console.log(
                                                             'onrowedited',
                                                             oldRow);
                                                         editAaoConfig({
                                                           variables: {
                                                             organisationId: this.props.organisationId,
                                                             uniqueAaoId: oldRow.uniqueId,
                                                             keywords: newRow.keywords.map(
                                                                 keyword => keyword.id),
                                                             locations: newRow.locations.map(
                                                                 location => location.id),
                                                             vehicles: newRow.vehicles.map(
                                                                 vehicle => vehicle.id),
                                                             timeRangeNames: []
                                                           }
                                                         })
                                                       }
                                                       }

                                                       onRowDeleted={(deletedRow) => {
                                                         console.log(
                                                             'deletedRow',
                                                             deletedRow);
                                                         deleteAaoConfig({
                                                           variables: {
                                                             organisationId: this.props.organisationId,
                                                             uniqueAaoId: deletedRow.uniqueId
                                                           }
                                                         })
                                                       }
                                                       }

                                        />
                                    )
                                  }}
                                </MutationHandler>
                            )
                          }}
                        </MutationHandler>
                    )
                  }}
                </MutationHandler>
            )
          }}
        </MutationHandler>
    );
  }
}

export default AaoEditMutation;