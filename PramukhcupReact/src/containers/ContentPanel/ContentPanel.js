import React, {Component} from 'react';
import {Route,NavLink} from 'react-router-dom';
import NewPlayer from '../../components/Players/NewPlayer/NewPlayer';
import DisplayPlayers from '../../components/Players/DisplayPlayers/DisplayPlayers';
import AuthorizePlayer from '../../components/admin/AuthorizePlayer/AuthorizePlayer';
import AboutUs from '../../components/AboutUs/AboutUs';
import './ContentPanel.css';
import Backdrop from '../../layout/UI/Backdrop/Backdrop'

const ContentPanel = (props) => {

  var sidebarClass = props.isSidebarOpen ? 'sidebar open' : 'sidebar';
  var contentClass = props.isSidebarOpen ? 'content open' : 'content';


    return (
      <div style={{ display: "flex" }} >
      <Backdrop clicked={props.sidebarClosed} show={props.isSidebarOpen}/>
      <div className={sidebarClass} onClick={props.sidebarClosed}>
      <ul>
      <li>
        <NavLink to="/Register" > Register </NavLink>
      </li>
      <li>
        <NavLink to="/DisplayPlayers" > Players </NavLink>
      </li>
      <li>
        <a href="https://docs.google.com/document/d/1EAGQ_uKRD7mDAaHUo6sr6Bnco7TBJA_c15cnaU5_Nlc/edit" target="_blank">Rule Book</a>
      </li>
      <li>
        <NavLink to="/AboutUs" > About Us </NavLink>
      </li>
      </ul>
      </div>
      <div>
        <Route path="/Register" exact component={NewPlayer}/>
        <Route path="/DisplayPlayers" exact component={DisplayPlayers}/>
        <Route path="/AuthPlayer" exact component={AuthorizePlayer}/>
        <Route path="/AboutUs" exact component={AboutUs}/>
        <Route path="/" exact component={AboutUs}/>
      </div>
      </div>
    );

}

export default ContentPanel;
