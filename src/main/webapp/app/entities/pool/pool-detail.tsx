import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pool.reducer';

export const PoolDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const poolEntity = useAppSelector(state => state.pool.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poolDetailsHeading">Pool</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{poolEntity.id}</dd>
          <dt>
            <span id="normal">Normal</span>
          </dt>
          <dd>{poolEntity.normal}</dd>
          <dt>
            <span id="hard">Hard</span>
          </dt>
          <dd>{poolEntity.hard}</dd>
          <dt>
            <span id="wiggle">Wiggle</span>
          </dt>
          <dd>{poolEntity.wiggle}</dd>
          <dt>
            <span id="expert">Expert</span>
          </dt>
          <dd>{poolEntity.expert}</dd>
        </dl>
        <Button tag={Link} to="/pool" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pool/${poolEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoolDetail;
