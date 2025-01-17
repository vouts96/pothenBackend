import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubmission, NewSubmission } from '../submission.model';

export type PartialUpdateSubmission = Partial<ISubmission> & Pick<ISubmission, 'id'>;

type RestOf<T extends ISubmission | NewSubmission> = Omit<T, 'acquisitionDate' | 'lossDate' | 'decisionDate'> & {
  acquisitionDate?: string | null;
  lossDate?: string | null;
  decisionDate?: string | null;
};

export type RestSubmission = RestOf<ISubmission>;

export type NewRestSubmission = RestOf<NewSubmission>;

export type PartialUpdateRestSubmission = RestOf<PartialUpdateSubmission>;

export type EntityResponseType = HttpResponse<ISubmission>;
export type EntityArrayResponseType = HttpResponse<ISubmission[]>;

@Injectable({ providedIn: 'root' })
export class SubmissionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/submissions');

  create(submission: NewSubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submission);
    return this.http
      .post<RestSubmission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(submission: ISubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submission);
    return this.http
      .put<RestSubmission>(`${this.resourceUrl}/${this.getSubmissionIdentifier(submission)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(submission: PartialUpdateSubmission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submission);
    return this.http
      .patch<RestSubmission>(`${this.resourceUrl}/${this.getSubmissionIdentifier(submission)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSubmission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSubmission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubmissionIdentifier(submission: Pick<ISubmission, 'id'>): number {
    return submission.id;
  }

  compareSubmission(o1: Pick<ISubmission, 'id'> | null, o2: Pick<ISubmission, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubmissionIdentifier(o1) === this.getSubmissionIdentifier(o2) : o1 === o2;
  }

  addSubmissionToCollectionIfMissing<Type extends Pick<ISubmission, 'id'>>(
    submissionCollection: Type[],
    ...submissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const submissions: Type[] = submissionsToCheck.filter(isPresent);
    if (submissions.length > 0) {
      const submissionCollectionIdentifiers = submissionCollection.map(submissionItem => this.getSubmissionIdentifier(submissionItem));
      const submissionsToAdd = submissions.filter(submissionItem => {
        const submissionIdentifier = this.getSubmissionIdentifier(submissionItem);
        if (submissionCollectionIdentifiers.includes(submissionIdentifier)) {
          return false;
        }
        submissionCollectionIdentifiers.push(submissionIdentifier);
        return true;
      });
      return [...submissionsToAdd, ...submissionCollection];
    }
    return submissionCollection;
  }

  protected convertDateFromClient<T extends ISubmission | NewSubmission | PartialUpdateSubmission>(submission: T): RestOf<T> {
    return {
      ...submission,
      acquisitionDate: submission.acquisitionDate?.format(DATE_FORMAT) ?? null,
      lossDate: submission.lossDate?.format(DATE_FORMAT) ?? null,
      decisionDate: submission.decisionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSubmission: RestSubmission): ISubmission {
    return {
      ...restSubmission,
      acquisitionDate: restSubmission.acquisitionDate ? dayjs(restSubmission.acquisitionDate) : undefined,
      lossDate: restSubmission.lossDate ? dayjs(restSubmission.lossDate) : undefined,
      decisionDate: restSubmission.decisionDate ? dayjs(restSubmission.decisionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSubmission>): HttpResponse<ISubmission> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSubmission[]>): HttpResponse<ISubmission[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
