import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoaderComponent } from './components/loader/loader.component';
import { LoaderService } from './services/loader.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [LoaderComponent],
  providers: [LoaderService],
  exports: [LoaderComponent]
})
export class LoaderModule { }
