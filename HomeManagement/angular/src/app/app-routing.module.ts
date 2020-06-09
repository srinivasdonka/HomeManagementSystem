import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from 'src/app/shared/components/login/login.component';
import { AppComponent } from 'src/app/app.component';
import { RegistrationComponent } from 'src/app/shared/components/registration/registration.component';
import { AcceptInvitationComponent } from 'src/app/shared/components/accept-invitation/accept-invitation.component';
import { VerifyEmailComponent } from 'src/app/shared/components/verify-email/verify-email.component';
import { ForgotPasswordComponent } from 'src/app/shared/components/forgot-password/forgot-password.component';
import { CheckEmailComponent } from 'src/app/shared/components/check-email/check-email.component';
import { ResetPasswordComponent } from 'src/app/shared/components/reset-password/reset-password.component';

export const routes: Routes = [
  { path: '', redirectTo: "/login", pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'login/:id', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'forgotpassword', component: ForgotPasswordComponent },
  { path: 'resetpassword/:id', component: ResetPasswordComponent },  
  { path: 'checkemail', component: CheckEmailComponent },
  { path: 'accept-invitation/:id', component: AcceptInvitationComponent },
  { path: 'verifyemail', component: VerifyEmailComponent },
  { path: 'admin', loadChildren: '../app/admin/admin.module#AdminModule' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
