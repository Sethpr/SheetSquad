import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stat.reducer';

export const StatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const statEntity = useAppSelector(state => state.stat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="statDetailsHeading">Stat</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{statEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{statEntity.type}</dd>
          <dt>Pool</dt>
          <dd>{statEntity.pool ? statEntity.pool.id : ''}</dd>
          <dt>Refrence</dt>
          <dd>{statEntity.refrence ? statEntity.refrence.id : ''}</dd>
          <dt>Extra</dt>
          <dd>
            {statEntity.extras
              ? statEntity.extras.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {statEntity.extras && i === statEntity.extras.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Owner</dt>
          <dd>
            {statEntity.owners
              ? statEntity.owners.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {statEntity.owners && i === statEntity.owners.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/stat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stat/${statEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StatDetail;
