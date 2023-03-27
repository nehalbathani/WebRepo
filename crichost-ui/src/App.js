import React, { Component } from 'react';
import './App.css';
import SideDrawer from './layout/SideDrawer/SideDrawer'
import Toolbar from './layout/Toolbar/Toolbar'
import NewPlayer from './components/Players/NewPlayer/NewPlayer'

class App extends Component {
  render() {
    return (
      <div className="App">
        <Toolbar />
        <SideDrawer/>
        <NewPlayer/>
      </div>
    );
  }
}

export default App;
