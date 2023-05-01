import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quality.reducer';

export const QualityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const qualityEntity = useAppSelector(state => state.quality.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="qualityDetailsHeading">Quality</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{qualityEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{qualityEntity.type}</dd>
          <dt>
            <span id="capacity1">Capacity 1</span>
          </dt>
          <dd>{qualityEntity.capacity1}</dd>
          <dt>
            <span id="capacity2">Capacity 2</span>
          </dt>
          <dd>{qualityEntity.capacity2}</dd>
          <dt>
            <span id="capacity3">Capacity 3</span>
          </dt>
          <dd>{qualityEntity.capacity3}</dd>
          <dt>
            <span id="cost">Cost</span>
          </dt>
          <dd>{qualityEntity.cost}</dd>
          <dt>Owner</dt>
          <dd>{qualityEntity.owner ? qualityEntity.owner.name : ''}</dd>
          <dt>Extra</dt>
          <dd>{qualityEntity.extra ? qualityEntity.extra.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/quality" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quality/${qualityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QualityDetail;
