import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './character.reducer';

export const CharacterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const characterEntity = useAppSelector(state => state.character.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="characterDetailsHeading">Character</h2>
        <dl className="jh-entity-details">
          <div className="container px-4 text-center">
            <div className="row gx-5">
              <div className="col">
                <div className="p-3">
                  <dt>
                    <span id="id">ID</span>
                  </dt>
                  <dd>{characterEntity.id}</dd>
                  <dt>
                    <span id="name">Name</span>
                  </dt>
                  <dd>{characterEntity.name}</dd>
                  <dt>
                    <span id="talentName">Talent Name</span>
                  </dt>
                  <dd>{characterEntity.talentName}</dd>
                  <table className="table">
                    <thead>
                      <tr>
                        <th scope="col">Stat</th>
                        <th scope="col">Pool</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <th scope="row">Body</th>
                        <td>4d</td>
                      </tr>
                      <tr>
                        <th scope="row">Coordination</th>
                        <td>4d</td>
                      </tr>
                      <tr>
                        <th scope="row">Sense</th>
                        <td>4d</td>
                      </tr>
                      <tr>
                        <th scope="row">Mind</th>
                        <td>4d</td>
                      </tr>
                      <tr>
                        <th scope="row">Charm</th>
                        <td>4d</td>
                      </tr>
                      <tr>
                        <th scope="row">Command</th>
                        <td>4d</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div className="col">
                <div className="p-3">
                  <dt>
                    <span id="loyalty">Loyalty</span>
                  </dt>
                  <dd>{characterEntity.loyalty}</dd>
                  <dt>
                    <span id="passion">Passion</span>
                  </dt>
                  <dd>{characterEntity.passion}</dd>
                  <dt>
                    <span id="inventory">Inventory</span>
                  </dt>
                  <dd>{characterEntity.inventory}</dd>
                  <dt>
                    <span id="pointTotal">Point Total</span>
                  </dt>
                  <dd>{characterEntity.pointTotal}</dd>
                  <dt>
                    <span id="spentPoints">Spent Points</span>
                  </dt>
                  <dd>{characterEntity.spentPoints}</dd>
                  <dt>Archetype</dt>
                  <dd>{characterEntity.archetype ? characterEntity.archetype.id : ''}</dd>
                  <dt>Owner</dt>
                  <dd>{characterEntity.owner ? characterEntity.owner.login : ''}</dd>
                </div>
              </div>
            </div>
          </div>
        </dl>
        <Button tag={Link} to="/character" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/character/${characterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CharacterDetail;
