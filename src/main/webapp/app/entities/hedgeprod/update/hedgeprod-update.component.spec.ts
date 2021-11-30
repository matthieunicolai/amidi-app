jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HedgeprodService } from '../service/hedgeprod.service';
import { IHedgeprod, Hedgeprod } from '../hedgeprod.model';

import { HedgeprodUpdateComponent } from './hedgeprod-update.component';

describe('Hedgeprod Management Update Component', () => {
  let comp: HedgeprodUpdateComponent;
  let fixture: ComponentFixture<HedgeprodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hedgeprodService: HedgeprodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HedgeprodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(HedgeprodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HedgeprodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hedgeprodService = TestBed.inject(HedgeprodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const hedgeprod: IHedgeprod = { id: 456 };

      activatedRoute.data = of({ hedgeprod });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(hedgeprod));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hedgeprod>>();
      const hedgeprod = { id: 123 };
      jest.spyOn(hedgeprodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hedgeprod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hedgeprod }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hedgeprodService.update).toHaveBeenCalledWith(hedgeprod);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hedgeprod>>();
      const hedgeprod = new Hedgeprod();
      jest.spyOn(hedgeprodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hedgeprod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hedgeprod }));
      saveSubject.complete();

      // THEN
      expect(hedgeprodService.create).toHaveBeenCalledWith(hedgeprod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hedgeprod>>();
      const hedgeprod = { id: 123 };
      jest.spyOn(hedgeprodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hedgeprod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hedgeprodService.update).toHaveBeenCalledWith(hedgeprod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
