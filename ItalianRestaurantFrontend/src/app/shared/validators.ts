import {AbstractControl, FormControl, FormGroupDirective, NgForm, ValidationErrors, ValidatorFn} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";

export class FormStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

export function priceValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valid = /^[0-9]+(\.[0-9]{1,2})?$/.test(control.value);
    return valid ? null : { invalidPrice: true };
  };
}
