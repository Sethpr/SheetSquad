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
            <span id="skillType">Skill Type</span>
          </dt>
          <dd>{skillEntity.skillType}</dd>
          <dt>
            <span id="refrenceStat">Refrence Stat</span>
          </dt>
          <dd>{skillEntity.refrenceStat}</dd>
          <dt>Pool</dt>
          <dd>{skillEntity.pool ? skillEntity.pool.id : ''}</dd>
          <dt>Extra</dt>
          <dd>{skillEntity.extra ? skillEntity.extra.id : ''}</dd>
          <dt>Owner</dt>
          <dd>{skillEntity.owner ? skillEntity.owner.name : ''}</dd>
          <dt>Refrence</dt>
          <dd>{skillEntity.refrence ? skillEntity.refrence.id : ''}</dd>
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
