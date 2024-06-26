export class AdminAppConstants {
  public static appName = 'HomeManagement';

  public static adminSideBarMenuItems = [
    {
      routerLink: './usermanagement',
      customClass: '',
      label: 'User Management',
      roleObject: ''
    },
    {
      routerLink: './homemanagement',
      customClass: '',
      label: 'Home Management',
      roleObject: ''
    },
    {
      routerLink: './propertymanagement',
      customClass: '',
      label: 'Property Management',
      roleObject: ''
    },
    
    {
      routerLink: './homeBudget',
      customClass: '',
      label: 'Home Budget',
      roleObject: ''
    },
   
    {
      routerLink: './profile',
      customClass: 'd-block d-sm-none',
      label: 'My Profile',
      roleObject: ''
    },
    {
      routerLink: '/login',
      customClass: 'd-block d-sm-none',
      label: 'LOGOUT',
      roleObject: ''
    }
  ];
}
