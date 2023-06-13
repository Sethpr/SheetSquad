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
import { IExtra } from 'app/shared/model/extra.model';
import { getEntities as getExtras } from 'app/entities/extra/extra.reducer';
import { ICharacter } from 'app/shared/model/character.model';
import { getEntities as getCharacters } from 'app/entities/character/character.reducer';
import { IRefrence } from 'app/shared/model/refrence.model';
import { getEntities as getRefrences } from 'app/entities/refrence/refrence.reducer';
import { IStat } from 'app/shared/model/stat.model';
import { StatType } from 'app/shared/model/enumerations/stat-type.model';
import { getEntity, updateEntity, createEntity, reset } from './stat.reducer';

export const StatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pools = useAppSelector(state => state.pool.entities);
  const extras = useAppSelector(state => state.extra.entities);
  const characters = useAppSelector(state => state.character.entities);
  const refrences = useAppSelector(state => state.refrence.entities);
  const statEntity = useAppSelector(state => state.stat.entity);
  const loading = useAppSelector(state => state.stat.loading);
  const updating = useAppSelector(state => state.stat.updating);
  const updateSuccess = useAppSelector(state => state.stat.updateSuccess);
  const statTypeValues = Object.keys(StatType);

  const handleClose = () => {
    navigate('/stat');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPools({}));
    dispatch(getExtras({}));
    dispatch(getCharacters({}));
    dispatch(getRefrences({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...statEntity,
      ...values,
      pool: pools.find(it => it.id.toString() === values.pool.toString()),
      extra: extras.find(it => it.id.toString() === values.extra.toString()),
      owner: characters.find(it => it.id.toString() === values.owner.toString()),
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
          statType: 'BODY',
          ...statEntity,
          pool: statEntity?.pool?.id,
          extra: statEntity?.extra?.id,
          owner: statEntity?.owner?.id,
          refrence: statEntity?.refrence?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.stat.home.createOrEditLabel" data-cy="StatCreateUpdateHeading">
            Create or edit a Stat
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="stat-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Stat Type" id="stat-statType" name="statType" data-cy="statType" type="select">
                {statTypeValues.map(statType => (
                  <option value={statType} key={statType}>
                    {statType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="stat-pool" name="pool" data-cy="pool" label="Pool" type="select">
                <option value="" key="0" />
                {pools
                  ? pools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="stat-extra" name="extra" data-cy="extra" label="Extra" type="select">
                <option value="" key="0" />
                {extras
                  ? extras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="stat-owner" name="owner" data-cy="owner" label="Owner" type="select">
                <option value="" key="0" />
                {characters
                  ? characters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="stat-refrence" name="refrence" data-cy="refrence" label="Refrence" type="select">
                <option value="" key="0" />
                {refrences
                  ? refrences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stat" replace color="info">
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

export default StatUpdate;
