import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './skill.reducer';

export const SkillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const skillEntity = useAppSelector(state => state.skill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="skillDetailsHeading">Skill</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{skillEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{skillEntity.type}</dd>
          <dt>
            <span id="under">Under</span>
          </dt>
          <dd>{skillEntity.under}</dd>
          <dt>Pool</dt>
          <dd>{skillEntity.pool ? skillEntity.pool.id : ''}</dd>
          <dt>Refrence</dt>
          <dd>{skillEntity.refrence ? skillEntity.refrence.id : ''}</dd>
          <dt>Extra</dt>
          <dd>
            {skillEntity.extras
              ? skillEntity.extras.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {skillEntity.extras && i === skillEntity.extras.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Owner</dt>
          <dd>
            {skillEntity.owners
              ? skillEntity.owners.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {skillEntity.owners && i === skillEntity.owners.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/skill/${skillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SkillDetail;
