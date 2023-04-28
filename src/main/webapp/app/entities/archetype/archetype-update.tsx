import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchetype } from 'app/shared/model/archetype.model';
import { getEntity, updateEntity, createEntity, reset } from './archetype.reducer';

export const ArchetypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const archetypeEntity = useAppSelector(state => state.archetype.entity);
  const loading = useAppSelector(state => state.archetype.loading);
  const updating = useAppSelector(state => state.archetype.updating);
  const updateSuccess = useAppSelector(state => state.archetype.updateSuccess);

  const handleClose = () => {
    navigate('/archetype');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...archetypeEntity,
      ...values,
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
          ...archetypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.archetype.home.createOrEditLabel" data-cy="ArchetypeCreateUpdateHeading">
            Create or edit a Archetype
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="archetype-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="archetype-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label="Cost"
                id="archetype-cost"
                name="cost"
                data-cy="cost"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Notes" id="archetype-notes" name="notes" data-cy="notes" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/archetype" replace color="info">
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

export default ArchetypeUpdate;
