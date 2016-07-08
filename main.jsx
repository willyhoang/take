// main.js
var React = require('react');
var ReactDOM = require('react-dom');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var NavDropdown= require('react-bootstrap').NavDropdown;
var MenuItem = require('react-bootstrap').MenuItem;
var Table = require('react-bootstrap').Table;
var Panel = require('react-bootstrap').Panel;

var Container = React.createClass({
  render: function() {
    return (
      <div className="container theme-showcase">
        {this.props.children}
      </div>
    );
  }
});

const navBarInstance = (
  <Navbar fluid={true} bsStyle="inverse">
    <Navbar.Header>
      <Navbar.Brand>
        <a href="#">React-Bootstrap</a>
      </Navbar.Brand>
      <Navbar.Toggle />
    </Navbar.Header>
    <Navbar.Collapse>
      <Nav>
        <NavItem eventKey={1} href="#">Link</NavItem>
        <NavItem eventKey={2} href="#">Link</NavItem>
        <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
          <MenuItem eventKey={3.1}>Action</MenuItem>
          <MenuItem eventKey={3.2}>Another action</MenuItem>
          <MenuItem eventKey={3.3}>Something else here</MenuItem>
          <MenuItem divider />
          <MenuItem eventKey={3.3}>Separated link</MenuItem>
        </NavDropdown>
      </Nav>
      <Nav pullRight>
        <NavItem eventKey={1} href="#">Link Right</NavItem>
        <NavItem eventKey={2} href="#">Link Right</NavItem>
      </Nav>
    </Navbar.Collapse>
  </Navbar>

);

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

const shebang = (
  <Container>
    <h1>Classes aggregator</h1>
    <p className="lead">Easily look through classes from various dance studios in NYC.</p>
    {navBarInstance}
    <Panel>
      <DanceClassesTable data={data} fill/>
    </Panel>
  </Container>
);

ReactDOM.render(
  shebang,
  document.getElementById('example')
);
