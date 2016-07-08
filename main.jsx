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

const tableInstance = (
  <Table responsive striped fill>
    <thead>
      <tr>
        <th>#</th>
        <th>Table heading</th>
        <th>Table heading</th>
        <th>Table heading</th>
        <th>Table heading</th>
        <th>Table heading</th>
        <th>Table heading</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>1</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
      </tr>
      <tr>
        <td>2</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
      </tr>
      <tr>
        <td>3</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
        <td>Table cell</td>
      </tr>
    </tbody>
  </Table>
);

const shebang = (
  <Container>
    <h1>Classes aggregator</h1>
    <p className="lead">Easily look through classes from various dance studios in NYC.</p>
    {navBarInstance}
    <Panel>
      {tableInstance}
    </Panel>
  </Container>
);

ReactDOM.render(
  shebang,
  document.getElementById('example')
);
