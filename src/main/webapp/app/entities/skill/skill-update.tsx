import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPool } from 'app/shared/model/pool.model';
import { getEntities as getPools } from 'app/entities/pool/pool.reducer';
import { IRefrence } from 'app/shared/model/refrence.model';
import { getEntities as getRefrences } from 'app/entities/refrence/refrence.reducer';
import { IExtra } from 'app/shared/model/extra.model';
import { getEntities as getExtras } from 'app/entities/extra/extra.reducer';
import { ICharacter } from 'app/shared/model/character.model';
import { getEntities as getCharacters } from 'app/entities/character/character.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { SkillType } from 'app/shared/model/enumerations/skill-type.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';
import { getEntity, updateEntity, createEntity, reset } from './skill.reducer';

export const SkillUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pools = useAppSelector(state => state.pool.entities);
  const refrences = useAppSelector(state => state.refrence.entities);
  const extras = useAppSelector(state => state.extra.entities);
  const characters = useAppSelector(state => state.character.entities);
  const skillEntity = useAppSelector(state => state.skill.entity);
  const loading = useAppSelector(state => state.skill.loading);
  const updating = useAppSelector(state => state.skill.updating);
  const updateSuccess = useAppSelector(state => state.skill.updateSuccess);
  const skillTypeValues = Object.keys(SkillType);
  const statTypeValues = Object.keys(StatType);

  const handleClose = () => {
    navigate('/skill');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPools({}));
    dispatch(getRefrences({}));
    dispatch(getExtras({}));
    dispatch(getCharacters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...skillEntity,
      ...values,
      extras: mapIdList(values.extras),
      owners: mapIdList(values.owners),
      pool: pools.find(it => it.id.toString() === values.pool.toString()),
      refrence: refrences.find(it => it.id.toString() === values.refrence.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'ATHLETICS',
          under: 'BODY',
          ...skillEntity,
          pool: skillEntity?.pool?.id,
          refrence: skillEntity?.refrence?.id,
          extras: skillEntity?.extras?.map(e => e.id.toString()),
          owners: skillEntity?.owners?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.skill.home.createOrEditLabel" data-cy="SkillCreateUpdateHeading">
            Create or edit a Skill
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="skill-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type" id="skill-type" name="type" data-cy="type" type="select">
                {skillTypeValues.map(skillType => (
                  <option value={skillType} key={skillType}>
                    {skillType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Under" id="skill-under" name="under" data-cy="under" type="select">
                {statTypeValues.map(statType => (
                  <option value={statType} key={statType}>
                    {statType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="skill-pool" name="pool" data-cy="pool" label="Pool" type="select">
                <option value="" key="0" />
                {pools
                  ? pools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="skill-refrence" name="refrence" data-cy="refrence" label="Refrence" type="select">
                <option value="" key="0" />
                {refrences
                  ? refrences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Extra" id="skill-extra" data-cy="extra" type="select" multiple name="extras">
                <option value="" key="0" />
                {extras
                  ? extras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Owner" id="skill-owner" data-cy="owner" type="select" multiple name="owners">
                <option value="" key="0" />
                {characters
                  ? characters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/skill" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SkillUpdate;
