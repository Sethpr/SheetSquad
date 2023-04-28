import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './power-category.reducer';

export const PowerCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const powerCategoryEntity = useAppSelector(state => state.powerCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="powerCategoryDetailsHeading">Power Category</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{powerCategoryEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{powerCategoryEntity.name}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{powerCategoryEntity.priority}</dd>
          <dt>
            <span id="cost">Cost</span>
          </dt>
          <dd>{powerCategoryEntity.cost}</dd>
          <dt>Owner</dt>
          <dd>{powerCategoryEntity.owner ? powerCategoryEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/power-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/power-category/${powerCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PowerCategoryDetail;
