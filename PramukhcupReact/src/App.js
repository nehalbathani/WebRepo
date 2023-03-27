import ReactDOM from 'react-dom';
import React, { Component } from 'react';
import {BrowserRouter} from 'react-router-dom';
import './App.css';
import Toolbar from './layout/Toolbar/Toolbar';
import ContentPanel from './containers/ContentPanel/ContentPanel';

class App extends Component {
  state={
    sidebarOpen: false
  };
  constructor(props){
      super(props);
      document.title = "Avenel Cricket Club";
    }
  handleViewSidebar= () =>{
  	this.setState({sidebarOpen: !this.state.sidebarOpen});
  }
  closeSidebar=()=>{
    if(this.state.sidebarOpen){
    this.setState({sidebarOpen: !this.state.sidebarOpen});
    }
  }


  render() {
    return (
      <BrowserRouter>
      <div className="App">
          <meta charSet="utf-8"/>
          <meta name="viewport" content="width=device-width, initial-scale=1"/>
          <link href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.7/united/bootstrap.min.css" rel="stylesheet" integrity="sha384-pVJelSCJ58Og1XDc2E95RVYHZDPb9AVyXsI8NoVpB2xmtxoZKJePbMfE4mlXw7BJ" crossOrigin="anonymous"/>
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css"/>
        <Toolbar sidebarChange={this.handleViewSidebar} />
        <ContentPanel isSidebarOpen={this.state.sidebarOpen} sidebarClosed={this.closeSidebar}/>
      </div>
      </BrowserRouter>
    );
  }
}

export default App;
