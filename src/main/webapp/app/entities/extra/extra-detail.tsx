import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './extra.reducer';

export const ExtraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const extraEntity = useAppSelector(state => state.extra.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="extraDetailsHeading">Extra</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{extraEntity.id}</dd>
          <dt>
            <span id="multiplier">Multiplier</span>
          </dt>
          <dd>{extraEntity.multiplier}</dd>
          <dt>
            <span id="notes">Notes</span>
          </dt>
          <dd>{extraEntity.notes}</dd>
          <dt>
            <span id="capacity">Capacity</span>
          </dt>
          <dd>{extraEntity.capacity}</dd>
          <dt>Base</dt>
          <dd>{extraEntity.base ? extraEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/extra" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/extra/${extraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExtraDetail;
