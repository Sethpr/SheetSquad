import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './power.reducer';

export const PowerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const powerEntity = useAppSelector(state => state.power.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="powerDetailsHeading">Power</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{powerEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{powerEntity.name}</dd>
          <dt>
            <span id="cost">Cost</span>
          </dt>
          <dd>{powerEntity.cost}</dd>
          <dt>
            <span id="notes">Notes</span>
          </dt>
          <dd>{powerEntity.notes}</dd>
          <dt>Pool</dt>
          <dd>{powerEntity.pool ? powerEntity.pool.id : ''}</dd>
          <dt>Owner</dt>
          <dd>{powerEntity.owner ? powerEntity.owner.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/power" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/power/${powerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PowerDetail;
