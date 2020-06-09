import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { HttpModule } from '@angular/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { LoaderModule } from 'src/app/loader/loader.module';
import { AppConfigService, initLabels, initMessages } from 'src/app/app-config.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    AppRoutingModule,
    SharedModule,
    LoaderModule
  ],
  providers: [
    AppConfigService,
    {
      'provide': APP_INITIALIZER,
      'useFactory': initLabels,
      'deps': [AppConfigService],
      'multi': true
    },
    {
      'provide': APP_INITIALIZER,
      'useFactory': initMessages,
      'deps': [AppConfigService],
      'multi': true
    }
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }
