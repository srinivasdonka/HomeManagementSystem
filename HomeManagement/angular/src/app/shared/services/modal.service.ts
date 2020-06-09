import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { GenericModalComponent } from '../components/generic-modal/generic-modal.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  modalDismissTimer;
  modalRef: BsModalRef;
  constructor(private modalService: BsModalService) { }

  errorModalDefaults = {
    backdrop: true,
    keyboard: false,
    modalFade: true,
    class: 'fixed-bottom'
  };

  errorModalOptions = {
    closeButtonText: '',
    actionButtonText: 'Ok',
    headerText: 'Error',
    bodyText: 'An Error Occured',
    component: GenericModalComponent
  };

  // success
  successModalDefaults = {
    backdrop: true,
    keyboard: false,
    modalFade: true,
    class: 'fixed-bottom'
  };

  successModalOptions = {
    closeButtonText: '',
    actionButtonText: 'Ok',
    autoHideTime: 2500,
    headerText: 'Success',
    bodyText: 'Submitted Succesfully',
    component: GenericModalComponent
  };

  showErrorModal(customModalOptions?, customModalDefaults?) {
    if (!customModalDefaults) {
      customModalDefaults = {};
    }
    if (!customModalOptions) {
      customModalOptions = {};
    }
    customModalDefaults.backdrop = 'static';
    return this.open(customModalDefaults, customModalOptions, this.errorModalOptions, this.errorModalDefaults);
  }


  showSuccessModal(customModalOptions?, customModalDefaults?) {
    if (!customModalDefaults) {
      customModalDefaults = {};
    }
    if (!customModalOptions) {
      customModalOptions = {};
    }
    customModalDefaults.backdrop = 'static';
    const autoHideTime = customModalOptions.autoHideTime ?
      customModalOptions.autoHideTime :
      this.successModalOptions.autoHideTime;

    if (customModalOptions.autoHide) {
      this.modalDismissTimer = setTimeout(() => {
        return this.close();
      }, autoHideTime);
    }
    return this.open(customModalDefaults, customModalOptions, this.successModalOptions, this.successModalDefaults);
  }


  open(customModalDefaults, customModalOptions, defaultModalOptions, modalDefaults) {
    const tempModalDefaults = {};
    const tempModalOptions = { class: 'fixed-bottom' };

    Object.assign(tempModalDefaults, modalDefaults, customModalDefaults);
    Object.assign(tempModalOptions, defaultModalOptions, customModalOptions);
    this.modalRef = this.modalService.show(GenericModalComponent, tempModalDefaults);
    this.modalRef.content.modalOptions = tempModalOptions;

    return this.modalService;
  }
  
  showModal(customModalOptions?, customModalDefaults?) {
    const tempModalDefaults = {};
    const tempModalOptions = {};
    
    const modalDefaults = {
      backdrop: true,
      keyboard: false,
      modalFade: true,
      ignoreBackdropClick: true,
      class: 'fixed-bottom'
    };

    const modalOptions = {};

    Object.assign(tempModalDefaults, modalDefaults, customModalDefaults);
    Object.assign(tempModalOptions, modalOptions, customModalOptions);
    this.modalRef = this.modalService.show(customModalOptions, tempModalDefaults);
    this.modalRef.content.modalOptions = tempModalOptions;

    return this.modalRef;
  }

  close() {
    this.modalRef.hide();
  }
}
