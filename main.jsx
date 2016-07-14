// main.js
var $ = require('jquery');
var React = require('react');
var ReactDOM = require('react-dom');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var NavDropdown= require('react-bootstrap').NavDropdown;
var MenuItem = require('react-bootstrap').MenuItem;
var Table = require('react-bootstrap').Table;
var Panel = require('react-bootstrap').Panel;
var DatePicker = require("react-bootstrap-date-picker");
var FormGroup = require('react-bootstrap').FormGroup;
var FormControl = require('react-bootstrap').FormControl;
var Table = require('reactable').Table;
var Tr = require('reactable').Tr;
var Td = require('reactable').Td;
var Multiselect = require('react-bootstrap-multiselect');

var Container = React.createClass({
  render: function() {
    return (
      <div className="container theme-showcase">
        {this.props.children}
      </div>
    );
  }
});

var ClassesDatePicker = React.createClass({
  getInitialState: function(){
    return {
      date: this.props.initialDate
    };
  },
  handleChange: function(value) {
    // value is an ISO String.
    this.setState({
      date: value
    });
    this.props.callbackParent(value);
  },
  render: function(){
    return <FormGroup>
      <DatePicker value={this.state.date} onChange={this.handleChange} />
    </FormGroup>;
  }
});

function getUniqueStyles(classes) {
    var styles = classes.map(function(danceClass) {
      return danceClass.style.toLowerCase();
    });
    var uniqueStyles = styles.filter(
      function(item, pos) { return styles.indexOf(item) == pos; }
    )
    var sortedStyles = uniqueStyles.sort();
    var stylesData = {};
    sortedStyles.forEach(function(val) {
      stylesData[val] = true;
    });
    return stylesData;
}

var ClassesNavBar = React.createClass({

  getInitialState: function(){
    return {
      date: this.props.initialDate,
    };
  },
  onChildChange: function(updatedDate) {
    console.log("setting state for classes nav bar")
    this.setState({ date: updatedDate });
    console.log("calling parent (danceclassesbody) on child changed method")
    this.props.callbackParent(updatedDate);
  },
  createValueToSelectedMapList: function(stylesEnabledMap) {
    var stylesToSelectedList = Object.keys(stylesEnabledMap).map(function (style) {
      return {value: style, selected: stylesEnabledMap[style]}
    });
    return stylesToSelectedList;
  },
  render: function() {
    console.log("state stylesEnabledMap")
    console.log(this.state.stylesEnabledMap)
    return (
      <Navbar fluid={true} bsStyle="inverse">
        <Navbar.Header>
          <Navbar.Brand>
            <a href="#">React-Bootstrap</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav>
            <Navbar.Form pullLeft>
                <ClassesDatePicker initialDate={this.state.date} callbackParent={this.onChildChange} />
            </Navbar.Form>
          </Nav>
          <Nav pullRight>
            <Navbar.Form>
              <Multiselect data={this.createValueToSelectedMapList(this.props.stylesEnabledMap)} multiple includeSelectAllOption={true} onChange={this.props.multiselectParentCallback}/>
            </Navbar.Form>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
});

/**
 * Converts a JSON time into a human readable string.
 * @param {object} time represented in JSON
 */
function formatTime(jsonTime) {
  var ampm = jsonTime.hour >= 12 ? 'PM' : 'AM';
  var hour = jsonTime.hour % 12;
  if (hour == 0) { // hour '0' should actually be '12'
    hour = 12;
  }
  var minute = jsonTime.minute;
  minute = minute < 10 ? '0' + minute : minute; // add leading zeroes
  var strTime = hour + ':' + minute + ' ' + ampm;
  return strTime;
}

/**
 * Convert a JSON time into a 24 hour format string.
 */
function sortableTime(jsonTime) {
  var d = new Date();
  d.setHours(jsonTime.hour);
  d.setMinutes(jsonTime.minute);
  d.setSeconds(0);
  return d
}

/**
 * Converts a JSON date into a human readable string.
 * @param {object} date represented as JSON
 */
function formatDate(jsonDate) {
  return jsonDate.month + "/" +  jsonDate.day;
}

function formatDateForUrl(isoDate) {
  return isoDate.substring(0, 10);
}

var DanceClassesTable = React.createClass({
  render: function() {
    var danceClasses = this.props.data.map(function(rowJson) {
      return (
        <Tr>
          <Td column="Date">{formatDate(rowJson.date)}</Td>
          <Td column="Start Time" value={sortableTime(rowJson.startTime)}>
            {formatTime(rowJson.startTime)}
          </Td>
          <Td column="End Time">{formatTime(rowJson.endTime)}</Td>
          <Td column="Studio" value={rowJson.studio}>{rowJson.studio}</Td>
          <Td column="Instructor" value={rowJson.instructor}>
            {rowJson.instructor}
          </Td>
          <Td column="Style" value={rowJson.style}>{rowJson.style}</Td>
          <Td column="Level" value={rowJson.level}>{rowJson.level}</Td>
        </Tr>
      );
    });
    return (
      <Table className="table table-striped"
        sortable={["Start Time", "Studio", "Instructor", "Style", "Level"]}
        filterable={[
            {
                column: 'Style',
                filterFunction: function(contents, filter) {
                    var filters = filter.split(",")
                    return filters.indexOf(contents.toLowerCase()) > -1;
                }
            },
        ]}
        hideFilterInput>
        {danceClasses}
      </Table>
    );
  }
});

var DanceClassesBody = React.createClass({
  getInitialState: function() {
    var initialDate = new Date().toISOString();
    return {
      date: initialDate,
      classes: [],
      stylesEnabledMap: {}
    };
  },
  onChildChanged: function(updatedDate) {
    console.log("calling dance classes body on child changed...")
    this.setState({ date: updatedDate }, this.loadClassesForDate);
  },
  onMultiselectChange: function(option, checked, select) {
    var updatedMap = this.state.stylesEnabledMap;
    updatedMap[option.val()] = checked;
    this.setState({ stylesEnabledMap: updatedMap });
  },
  loadClassesForDate: function() {
    console.log("loading classes for date: " + this.state.date)
    var formattedDate = formatDateForUrl(this.state.date);
    var requestUrl = "http://take-prod.us-east-1.elasticbeanstalk.com/classes/" + formattedDate
    $.ajax({
      url: requestUrl,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({
          classes: data,
          stylesEnabledMap: getUniqueStyles(data)
        });
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  componentDidMount: function() {
    this.loadClassesForDate();
  },
  render: function() {
    return (
      <div>
        <ClassesNavBar initialDate={this.state.date} callbackParent={this.onChildChanged} stylesEnabledMap={this.state.stylesEnabledMap} multiselectParentCallback={this.onMultiselectChange}/>
        <Panel>
          <DanceClassesTable initialDate={this.state.date} data={this.state.classes} fill/>
        </Panel>
      </div>
    );
  }
});

const shebang = (
  <Container>
    <h1>Classes aggregator</h1>
    <p className="lead">Easily look through classes from various dance studios in NYC.</p>
    <DanceClassesBody />
  </Container>
);

ReactDOM.render(
  shebang,
  document.getElementById('example')
);
