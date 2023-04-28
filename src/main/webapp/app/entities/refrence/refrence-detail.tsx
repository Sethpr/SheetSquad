import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './refrence.reducer';

export const RefrenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const refrenceEntity = useAppSelector(state => state.refrence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="refrenceDetailsHeading">Refrence</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{refrenceEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{refrenceEntity.title}</dd>
          <dt>
            <span id="info">Info</span>
          </dt>
          <dd>{refrenceEntity.info}</dd>
        </dl>
        <Button tag={Link} to="/refrence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/refrence/${refrenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RefrenceDetail;
