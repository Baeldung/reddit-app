<div id="resubmit"> 
    <div class="form-group">
        <label class="col-sm-3">Resubmit If:</label>

        <div class="col-sm-2">Score is less than</div>
        <div class="col-sm-1">
            <input type="number" class="form-control input-sm" name="minScoreRequired" />
        </div>

        <div class="col-sm-3">
            every &nbsp;&nbsp; <select name="timeInterval">
                <option value="0">None</option>
                <option value="45">45 minutes</option>
                <option value="60">1 hour</option>
                <option value="120">2 hours</option>
            </select>
        </div>

        <div class="col-sm-3">
            for &nbsp;&nbsp; <select name="noOfAttempts">
                <option value="0">No</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select> &nbsp;&nbsp; attempts.
        </div>


    </div>
    <br /><br />

    <div class="form-group">
        <label class="col-sm-3">Keep If:</label>

        <div class="col-sm-2">Post has at least</div>
        <div class="col-sm-1">
            <input type="number" class="form-control input-sm" value="0" name="minTotalVotes" data-min="0" />
        </div>
        <div class="col-sm-3">Total votes</div>
        <div class="col-sm-3">
            Has Comments &nbsp;&nbsp;
            <input type="checkbox" name="keepIfHasComments" value="true" />
        </div>
    </div>
    
     <br /> 
     
    <div class="form-group">
        <label class="col-sm-3">Delete If:</label>
        <div class="col-sm-9">
            Consume all Attempts &nbsp;&nbsp;
            <input type="checkbox" name="deleteAfterLastAttempt" value="true" />
        </div>

    </div>
    <br /> 
<script>
function resetResubmitOptions(){
    $('#resubmit  select').each(function () { $(this).val(0)});
    $('#resubmit  input[type="number"]').each(function () {$(this).val(0)});
    $('#resubmit  input[type="checkbox"]').each(function () {$(this).removeAttr("checked")});
}
</script>
</div>