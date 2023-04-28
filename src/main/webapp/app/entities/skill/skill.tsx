import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISkill } from 'app/shared/model/skill.model';
import { getEntities } from './skill.reducer';

export const Skill = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const skillList = useAppSelector(state => state.skill.entities);
  const loading = useAppSelector(state => state.skill.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="skill-heading" data-cy="SkillHeading">
        Skills
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/skill/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Skill
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {skillList && skillList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Under</th>
                <th>Pool</th>
                <th>Refrence</th>
                <th>Extra</th>
                <th>Owner</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {skillList.map((skill, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/skill/${skill.id}`} color="link" size="sm">
                      {skill.id}
                    </Button>
                  </td>
                  <td>{skill.type}</td>
                  <td>{skill.under}</td>
                  <td>{skill.pool ? <Link to={`/pool/${skill.pool.id}`}>{skill.pool.id}</Link> : ''}</td>
                  <td>{skill.refrence ? <Link to={`/refrence/${skill.refrence.id}`}>{skill.refrence.id}</Link> : ''}</td>
                  <td>
                    {skill.extras
                      ? skill.extras.map((val, j) => (
                          <span key={j}>
                            <Link to={`/extra/${val.id}`}>{val.id}</Link>
                            {j === skill.extras.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {skill.owners
                      ? skill.owners.map((val, j) => (
                          <span key={j}>
                            <Link to={`/character/${val.id}`}>{val.id}</Link>
                            {j === skill.owners.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/skill/${skill.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/skill/${skill.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/skill/${skill.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Skills found</div>
        )}
      </div>
    </div>
  );
};

export default Skill;
