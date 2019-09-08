import React, {Component} from 'react';
import EditableTable from "./editableTable";
import Pagination from "react-bootstrap/Pagination";

class PagedEditableTable extends Component {

  constructor(props) {
    super(props);
  }

  pageChanged(e) {
    let pageNumber = e.target.text;
    let pageIndex = pageNumber - 1;
    if (this.props.onPageRequested) {
      this.props.onPageRequested({currentPage: pageIndex})
    }
  }

  render() {
    return (
        <div>
          <EditableTable data={this.props.data}
                         columns={this.props.columns}
                         canEdit={this.props.canEdit}
                         canDelete={this.props.canDelete}
                         canCreate={this.props.canCreate}
                         canView={this.props.canView}

                         onNewRow={newRow => {
                           this.props.onNewRow
                           && this.props.onNewRow(newRow);
                         }}

                         onRowEdited={(oldRow, newRow) =>
                             this.props.onRowEdited
                             && this.props.onRowEdited(oldRow, newRow)}

                         onRowDeleted={(deletedRow) =>
                             this.props.onRowDeleted
                             && this.props.onRowDeleted(deletedRow)}

                         onRowViewed={(row) =>
                             this.props.onRowViewed
                             && this.props.onRowViewed(row)}

                         onStartEditMode={(row) =>
                             this.props.onStartEditMode
                             && this.props.onStartEditMode(row)}

          />
          <Pagination onClick={p => this.pageChanged(p)}>
          {
            [...Array(this.props.totalPages).keys()].map(pageIndex =>
            {
              let page = pageIndex + 1;
              let isCurrentPage = pageIndex == this.props.currentPage;
              return (
                  <Pagination.Item key={page} active={isCurrentPage}>
                    {page}
                  </Pagination.Item>
              )
            })
          }
          </Pagination>
        </div>)
  }
}

export default PagedEditableTable;