import { Pipe, PipeTransform } from '@angular/core'
import * as moment from 'moment'

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  transform(date: any, args?: any): any {
    let d = new Date(date * 1000);
    return moment(d).format('MMM Do, h:mm a, YYYY');
  }

}
