export class RegularExp {

  constructor() { }
  
  public static get emailPattern() {
    // /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
    return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  }

  public static get passwordPattern() {
    return /(?=.*[0-9]).{6,24}$/
    // const passwordPattern = '((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,24})';
  }


  public static get endsWithNumberInParens() {
    return /(\s+)\((\d+)\)$/; // find if the string ends with (digits)
  }

  public static get stringBetweenDoubleCurlybraces() {
    return /{{\s*([^}]+)\s*}}/g; // find string between curly braces
  }
}
