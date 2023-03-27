import React, {Component} from 'react';
import Grid from 'react-bootstrap/lib/Grid';
import Row from 'react-bootstrap/lib/Row';
import Col from 'react-bootstrap/lib/Col';
import {Route,NavLink} from 'react-router-dom';
import NewPlayer from '../../components/Players/NewPlayer/NewPlayer';

const AboutUs = () => {

    return (
      <div >
      <Grid>
      <Row>
        <Col xs={18} md={12}>
        <h3>Avenel Cricket club for Woodbridge community  </h3>
        <hr/>
        </Col>
      </Row>
      <Row>
      <Col xs={2} md={2}></Col>
      <Col xs={10} md={8}>
        <p>Avenel Cricket club helps people to improve their physical and mental strength through the medium of cricket and help every individual to take their game to next level by providing equal opportunity irrespective of a person&apos;s skill and age. At this place, people again become like kids and play cricket competitively.</p>
        <p>This league and group started playing cricket at Avenel middle school since 2009 and that&apos;s why club name is Avenel Cricket Club and league name is Avenel Cricket League. But now it is not limited to Avenel. Players from all towns of new jersey is the part of this club. It was started with 15-20 players in 2009 and right now more than 100 members plays cricket under this club. Players like this club more than any other big professional league because of its semiprofessional nature where not only high skilled players get the chance to play but all players from every age group and different level of skill, gets the chance to play and improve their game of cricket. Please refer  <a href="https://docs.google.com/document/d/1EAGQ_uKRD7mDAaHUo6sr6Bnco7TBJA_c15cnaU5_Nlc/edit" target="_blank">rule book</a> which clearly mentions how it makes sure to give all players equal chance and make sure they spend quality time on ground.
        </p>
        <p>
          If you are interested, please click here to <NavLink to="/Register" > Register </NavLink>
        </p>
      </Col>
      </Row>
      </Grid>


      </div>
    );

}

export default AboutUs;
