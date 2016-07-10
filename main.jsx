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

var ClassesNavBar = React.createClass({
  getInitialState: function(){
    return {
      date: this.props.initialDate
    };
  },
  onChildChange: function(updatedDate) {
    console.log("setting state for classes nav bar")
    this.setState({ date: updatedDate });
    console.log("calling parent (danceclassesbody) on child changed method")
    this.props.callbackParent(updatedDate);
  },
  render: function() {
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
            <NavItem eventKey={1} href="#">Link Right</NavItem>
            <NavItem eventKey={2} href="#">Link Right</NavItem>
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
 * Converts a JSON date into a human readable string.
 * @param {object} date represented as JSON
 */
function formatDate(jsonDate) {
  return jsonDate.month + "/" +  jsonDate.day;
}

var DanceClass = React.createClass({
  render: function() {
    return (
      <tr>
        <td>{formatDate(this.props.date)}</td>
        <td>{formatTime(this.props.startTime)}</td>
        <td>{formatTime(this.props.endTime)}</td>
        <td>{this.props.studio}</td>
        <td>{this.props.instructor}</td>
        <td>{this.props.style}</td>
      </tr>
    );
  }
});

function formatDateForUrl(isoDate) {
  return isoDate.substring(0, 10);
}

var DanceClassesTable = React.createClass({
  render: function() {
    var danceClasses = this.props.data.map(function(rowJson) {
      return (
        <DanceClass date={rowJson.date} startTime={rowJson.startTime} endTime={rowJson.endTime} studio={rowJson.studio} instructor={rowJson.instructor} style={rowJson.style}/>
      );
    });
    return (
      <Table responsive striped fill>
        <thead>
          <tr>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Studio</th>
            <th>Instructor</th>
            <th>Style</th>
          </tr>
        </thead>
        <tbody>
          {danceClasses}
        </tbody>
      </Table>
    );
  }
});

var data = [
  {
date: {
year: 2016,
month: 7,
day: 8
},
startTime: {
hour: 9,
minute: 0,
second: 0,
millis: 0
},
endTime: {
hour: 10,
minute: 30,
second: 0,
millis: 0
},
studio: "BDC",
instructor: "Greg Zane",
style: "BALLET",
level: "Adv Beg"
},
{
date: {
year: 2016,
month: 7,
day: 8
},
startTime: {
hour: 9,
minute: 0,
second: 0,
millis: 0
},
endTime: {
hour: 10,
minute: 30,
second: 0,
millis: 0
},
studio: "BDC",
instructor: "Eric Campros^ -Canceled",
style: "CONTEMPORARY JAZZ",
level: "Bas"
},
];

var DanceClassesBody = React.createClass({
  getInitialState: function() {
    var initialDate = new Date().toISOString();
    return {
      date: initialDate,
      data: []
    };
  },
  onChildChanged: function(updatedDate) {
    console.log("calling dance classes body on child changed...")
    this.setState({ date: updatedDate }, this.loadClassesForDate);
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
        this.setState({data: data});
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
        <ClassesNavBar initialDate={this.state.date} callbackParent={this.onChildChanged}/>
        <Panel>
          <DanceClassesTable initialDate={this.state.date} data={this.state.data} fill/>
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
